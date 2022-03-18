第三天项目进度
    需要补的代码内容：
        1、登录成功后默认显示的页面更改为controller请求   √
        2、更改市场活动导航的地址   √
        3、市场活动中给创建添加单击事件，弹出模态窗口
        4、给模态窗口的创建人下拉框赋值（从controller中获取，不要包含user状态为0的数据）
        5、给工作页面的右上角添加当前登录人的名称信息
        6、设置创建模态窗口的所有者和名称不为空
        7、开始日期和结束日期不为空的情况下结束日期比开始日期大
        8、成本为正整数
        9、点击保存按钮验证表单，通过则提交表单，后台保存至数据库
        10、通过mybatis逆向工程生成activity的相关类和接口及mapper
        11、

1、打开市场活动页面时向服务器发送请求获取下拉框中的拥有者信息，即登录用户信息、

    1）从用jstl向下拉框中填充数据，并且将当前登录用户设置为默认选中状态
    2）使用js代码在单击创建时打开模态窗口

2、生成activity表的逆向工程


3、填写完成添加项目，点击保存
    1）验证所有者和项目名称
    2）验证开始时间和结束时间
    3）验证成本
    4）向后台发起请求保存项目内容
    5）创建一个生成UUID的工具类
    6）创建controller方法
    7）生成UUID、创建时间、创建人，放入activity对象中传递至service层
    8）编写service层，调用mapper
    9）书写SQL语句
    10）第二次打开创建项目前清空上一次填写的内容
    注意事项：在jsp中注意对象选择器的用法

4、日历插件的使用

5、分页查询（分析需求）
    1）加载市场活动页面时发送请求获取所有活动的列表，并且显示第一页，默认每页显示条数10条，默认第一页
        需要参数为页数pageNo，每页显示条数，pageSize
        返回的数据需要包含：活动列表的数组，总条数，总页数
    2）controller接收参数
        service中编写两个方法：查询活动列表的方法，查询活动总条数的方法
        将参数传递至dao
    3）编写sql语句，
        查询活动列表的sql：
            select id,owner,name,start_date,end_date from tbl_activity order by createTime limit (pageNo-1)*PageSize,pageSize
        查询总条数的sql：
            select count(id) from tbl_activity
    4)controller接收返回的数据，封装到一个pageVO中
        pageVO包含：总条数信息total，总页数信息pageCount，活动集合ActivityList；
    5）使用ResponseBody返回pageVO
    6）在jsp中使用jQuery的EL表达式以及JSTL标签将数据展示







