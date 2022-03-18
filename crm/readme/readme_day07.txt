第七天项目进度

1、查看市场活动明细
    需求分析：
        用户在市场活动主页面,点击市场活动名称超链接,跳转到明细页面,完成查看市场活动明细的功能.
    	*在市场活动明细页面,展示:
            -市场活动的基本信息
            -该市场活动下所有的备注信息

    代码实现：
        1、给超链接赋予当前市场活动的id，随着超链接一起提交，因为要跳转页面，所以是同步请求
        2、controller接收请求
            1》调用activityService去查询对应市场活动的详细信息
            2》调用ActivityRemarkService去查询这个市场活动对应的所有备注信息
            3》取得返回的数据封装到request作用域中
            4》跳转页面
        3、activityService
            1》调用activityMapper
            2》编写SQL语句获取这条id对应的详细信息
        4、调用activityRemarkService
            1》调用activityRemarkMapper
            2》编写sql语句获取这个id对应的所有备注信息
        5、activity/detail.jsp中接收数据
        6、通过EL表达式将数据填写到页面中

2、添加市场活动备注
    需求分析：
        用户在市场活动明细页面,输入备注内容,点击"保存"按钮,完成添加市场活动备注的功能.
            *备注内容不能为空
            *添加成功之后,清空输入框,刷新备注列表
            *添加失败,提示信息,输入框不清空,列表也不刷新

    代码实现：
        1、填写完备注后点击保存按钮，先判断备注是否为空，不为空发送ajax请求，将备注内容和市场活动id作为参数发送
        2、controller接收请求
            1》将参数封装到ActivityRemark对象中取，并且在controller中继续封装需要的其他信息：id,createBy,createTime...
            2》调用Service并接收返回结果
            3》因为是写数据，所以需要使用try..catch..
            4》根据返回结果往ReturnObject中写入对应数据，如果成功，还需要将ActivityRemark对象页写入到ReturnObject中，用来给前端刷新数据用
            5》返回ReturnObject
        3、service接收请求
        4、mapper接收请求，编写插入的sql语句
        5、jsp中接收到返回数据后，判断成功与否，成功的话，清除添加备注框填写的信息，拼接一个备注信息的div，可以使用append追加到备注信息的父div中，也可以使用before在添加备注的div前面添加上这个备注
        6、失败的话弹出错误信息


       （以下为自己实现的过程，与上面稍有不同，主要是传输数据量的大小和查询数据库的次数不同，稍微麻烦了一些）
        1、填写完备注后点击保存按钮，先判断备注是否为空，不为空发送ajax请求，将备注内容和市场活动id作为参数发送
        2、controller接收请求
            1》将参数封装到ActivityRemark对象中取，并且在controller中继续封装需要的其他信息：id,createBy,createTime...
            2》调用ActivityRemarkService并接收返回插入结果
            3》调用ActivityRemarkService查询这个市场活动id的所有备注信息封装到list中返回到controller中
            4》根据插入的返回结果往ReturnObject中写入对应数据，如果成功，还需要将备注list写入到ReturnObject中，用来给前端刷新数据用
            5》返回ReturnObject
        3、service接收请求
        4、mapper接收请求，编写插入的sql语句
        5、jsp中接收到返回数据后，判断成功与否，成功的话，清除添加备注框填写的信息，删除已显示备注信息的大div【自建的父div】中的所有备注信息，循环ReturnObject中的备注list，
            拼接成备注信息的字符串，使用append添加到显示备注信息的大div【自建的父div】中，
        6、失败的话弹出错误信息