第六天项目进度

技术支持：
    生成文件
        使用poi插件，利用插件中的类HSSFWorkbook创建文件，往文件中填写对应的行数据，列数据
        注意：
            1、生成文件通过controller下载至浏览器时，controller方法的返回值必须是void，且不能在方法上添加@ResponseBody注解
            2、需要设置返回数据类型，通过response设置response.setContentType("application/octet-stream;charset=UTF-8")
            3、需要设置Header，作用是让浏览器以设置的方式处理文件，通过response设置response.setHeader("Content-Disposition","attachment;filename=xxxx.xls")
                需要注意filename后面的文件名不能是中文，否则下载后的文件不会显示名称，会把格式作为名称显示，且文件无格式。
            4、通过HSSFWorkbook对象生成的文件可以直接通过response获取的输出流向浏览器发送文件，不需要浪费效率写入到磁盘

    文件上传
        1、在前端页面中文件上传表单必须以post请求的方式发送
        2、form表单中必须指定表单的编码方式/加密类型：enctype="multipart/form-data"
        3、controller接收数据时，除了文件数据，同时也可以包含表单中的其他数据String
        4、controller方法参数中必须使用MultipartFile接收这个文件数据，并且需要在mvc配置文件中声明文件上传解析器：如下，id:必须是multipartResolver
            <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
                    <property name="maxUploadSize" value="#{1024*1024*5}"/>
                    <property name="defaultEncoding" value="utf-8"/>
            </bean>
        5、MultipartFile类中通过transferTo("D:\\WorkSpeace\\xueshen.xls")将文件写入磁盘目录中
        6、最后的原文件名和文件类型可以根据MultipartFile类中的getOriginalFilename()方法获取，此方法返回文件的原名.文件类型，如果想取得任意一部分，可以通过截取字符串的方式获得

    文件解析
        依然使用poi，反过来从表格中取数据
        1、HSSFWorkbook对象在创建时，可以传递一个输入流，这个输入流就是读取对应文件的流
        2、通过HSSFWorkbook获取sheet页
        3、通过页sheet获取行row
        4、通过行获取列cell
        5、从中取出对应的行列数据，需要判断取出的数据的类型，可以通过HSSFCell中封装的常数判断对应数据的类型，可封装至工具类的方法
        6、将取出的数据封装至对象中，最终添加到集合中
        7、保存至数据库
        注意：1、文件中添加数据的字段最好能直接保存到数据库
             2、如果不能直接保存到数据库，比如id，create_by,owner等需要存储UUID这种后台生成的随机的样式，就不要放在文件的约定字段中


1、批量导出
    分析需求：
        1）前端发起请求，后台查询到所有的活动数据，通过poi插件将活动数据放到.xls类型的文件中，其内部实际为表格样式
        2）poi插件将数据封装到一个表格文件类中，未优化：通过输出流写入到服务器硬盘中，后面再从硬盘中读取；优化后：不往硬盘写，直接传入response创建的输出流将文件传至浏览器
        3）通过response设置输出数据的类型ContentType：application/x-xls;charset=UTF-8,或者是application/octet-stream;charset=UTF-8
        4）通过response设置信息头Header信息，用于让浏览器知道对这个文件的处理方式：参数为：("Content-Disposition","attachment;filename=xxxx.xls")
        5）未优化时：创建一个输入流，将前面写入到磁盘的文件读取到内存，再通过response创建的输出流传至浏览器
            优化后，将response创建的输出流传至工作文件的write方法中作为参数即可，
            对比：
            优化前：文件创建好之后还需要往内存中写一边，此过程效率很低，且需要占用服务器内存，最后往浏览器传时，还需要从磁盘中读取到内存中，此过程效率页很低，再从内存发送至浏览器
            优化后：节省了往磁盘中写和从磁盘中读数据的过程，直接在内存中由一块内存放置到另一块内存中然后传递至浏览器，内存中传递效率高了很多
        6）浏览器接收到文件数据选择操作方式：保存/打开

    代码实现：
        1）前端发起请求至controller，通过controller调用service再到dao去取所有活动的数据信息
        2）编写sql语句，获取所有的活动信息，其中，需要将连接其他表中id的数据转换为可阅读的数据
        3）controller获取到封装着所有活动数据的数组
        4）先通过poi插件画出表的表头信息和表名等信息
        5）循环活动数组，一边生成列和行，一边往此列中写入对应行对应列的数据信息
        6）通过response设置ContentType属性
        7）通过response设置Header属性
        8）通过response获取到文件输出流
        9）将文件输出流传递至生成文件的对象workbook中，传递数据
        10）关闭workbook对象自身的流
        11）刷新文件输出流，但不用关闭，因为他是response取的，也由其关闭

2、选择导出
    分析需求：
        1）前端点击选择导出，验证选中的活动数量，至少一个
        2）遍历选中的活动，将其id拼接为url中参数的样式
        3）发起同步请求，后面携带拼接的参数信息，传递至service，dao查询到所需的活动数据封装到list中返回
        4）controller接收数据，通过HSSFWorkbook新建新的表格页，先添加表头，再通过for循环将list数据取出写入到对应的行列中
        5）通过response设置ContentType属性：response.setContentType("application/octet-stream;charset=UTF-8")
        6）通过response设置Header：response.setHeader("Content-Disposition","attachment;filename=xxx.xls")
        7）通过response获取输出流outputStream
        8）通过HSSFWorkbook的write()写数据，传到浏览器


3、导入文件(浏览器->服务器)
    需求分析：
        1、用户在市场活动主页面,点击"导入"按钮,弹出导入市场活动的模态窗口;
        2、用户在导入市场活动的模态窗口选择要上传的文件,点击"导入"按钮,完成导入市场活动的功能.
        3、*只支持.xls
        4、*文件大小不超过5MB
        5、*导入成功之后,提示成功导入记录条数,关闭模态窗口,刷新市场活动列表,显示第一页数据,保持每页显示条数不变
        6、*导入失败,提示信息,模态窗口不关闭,列表也不刷新

    代码实现：
        1、

    实现过程中注意点：
        1、通过id获取器获取的type="file"的value实际上只是这个文件的名称，而需要取得这个文件(文件本身)，需要将这个元素先转换成dom对象，在dom对象中有files属性，
            取files[0]就是这个文件本身
        2、需要判断导入的文件类型可以根据文件的名称截取最后的字符来确定文件的类型，因为文件类型名可能为大写，也可能为小写，或者大小写都有的情况，所以在截取字符串的后面需要将大小写统一
        3、文件大小的判断可以由文件本身的属性size获取，单位为字节
        4、通过ajax上传文件需要将文件添加到ajax中的一个接口(类)FormData中,这个接口不但能提交文本数据，还能提交二进制数据，所以文件需要使用这个类当载体来进行提交，
            使用append添加数据，参数为键值对的方式，key部分与controller方法中的获取参数名要一致
        5、通过ajax上传文件，需要更改参数processData:false,这个属性的意思是ajax在发送请求提交参数之前，是否默认把参数转化成字符串。true:是；false：不是，默认为true
        6、ajax上传文件还需要更改contentType:false，这个属性设置ajax在提交参数之前，是否把所有参数统一按urlencoded编码。true：是；false：不是，默认为true

       优化：
            原本文件上传至服务器后是先从内存中写入到磁盘中去，在从磁盘中读取到内存，拿取文件中的数据内容才包装数据，涉及到磁盘读写操作，效率很低
            而本身参数位置的MultipartFile中就有一个getInputStream()方法，用来读取上传的文件，然后直接交给HSSFWorkbook对象读取文件内容，不涉及磁盘读写，效率提高了

       bug：
        在要导入的文件中修改数据，如果删除掉原本有数据的行空着，会使得这个行在HSSFWorkbook对象中有这个空行的数据，接下来读取在cell里会发生空指针异常
       bug解决：
            在内层循环中判断cell的值，如果一旦cell为空或者为null，就使用continue跳出外层循环，这样就不会再new出新的市场活动，也就不会再插入空数据
            如果只使用了break或者continue跳出了内层循环，那么还是会new出市场活动的对象,但是不会给表格里的相关数据赋值，所以会往数据库里插入对应多的空数据（除了id，owner等外边赋值的）