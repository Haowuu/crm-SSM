package com.wh.crm.workbench.web.controller;

import com.wh.crm.commons.contants.Contants;
import com.wh.crm.commons.domain.ReturnObject;
import com.wh.crm.commons.utils.DateUtils;
import com.wh.crm.commons.utils.UUIDUtils;
import com.wh.crm.settings.domain.User;
import com.wh.crm.settings.service.UserService;
import com.wh.crm.workbench.domain.Activity;
import com.wh.crm.workbench.domain.ActivityRemark;
import com.wh.crm.workbench.service.ActivityRemarkService;
import com.wh.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * @author wu
 * @createTime 2022/3/5  17:08
 * @description
 */
@Controller
public class ActivityController {
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityRemarkService activityRemarkService;

    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request) {
        List<User> userList = userService.queryAllUser();
        request.setAttribute("userList", userList);
        return "workbench/activity/index";
    }


    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    @ResponseBody
    public Object saveCreateActivity(Activity activity, HttpSession session) {
        /*
         * 1、生成UUID
         * 2、生成createTime
         * 3、生成createBy
         * */
        String id = UUIDUtils.getUUID();
        String createTime = DateUtils.formatDateTime(new Date());
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        String createBy = user.getId();
        activity.setId(id);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);

        int a = activityService.saveCreateActivity(activity);

        ReturnObject returnObject = new ReturnObject();
        if (a > 0) {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        } else {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMassage("系统正忙，请稍后再试");
        }

        return returnObject;
    }

    /**
     * 分页查询
     */
    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    @ResponseBody
    public Object queryActivityByConditionForPage(String name, String owner, String startDate, String endDate, String pageNo, String pageSize) {

        Integer pageNos = Integer.valueOf(pageNo);
        Integer pageSizes = Integer.valueOf(pageSize);
        Integer limitCount = (pageNos - 1) * pageSizes;
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("pageSize", pageSizes);
        map.put("limitCount", limitCount);
        List<Activity> activityList = activityService.queryActivityByConditionForPage(map);
        int total = activityService.queryTotalByCondition(map);

        Map<String, Object> retMap = new HashMap<>();
        retMap.put("activityList", activityList);
        retMap.put("total", total);
        return retMap;
    }

    @RequestMapping("/workbench/activity/deleteActivityByIds.do")
    @ResponseBody
    public Object deleteActivityByIds(String[] id) {
        ReturnObject returnObject = new ReturnObject();
        try {
            int num = activityService.deleteActivityByIds(id);
            if (num > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMassage("系统繁忙，请稍后重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMassage("系统繁忙，请稍后重试");
        }

        return returnObject;
    }

    @RequestMapping("/workbench/activity/queryActivityById.do")
    @ResponseBody
    public Object queryActivityById(String id) {
        Activity activity = activityService.queryActivityById(id);

        /*ReturnObject returnObject = new ReturnObject();

        if (activity == null) {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMassage("系统繁忙，请稍后重试");
        } else {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setReturnData(activity);
        }*/
        return activity;
    }
    /**
     * 保存更新的市场活动
     * */
    @RequestMapping("/workbench/activity/saveUpdateActivity.do")
    @ResponseBody
    public Object saveUpdateActivity(Activity activity, HttpSession session) {

        activity.setEditTime(DateUtils.formatDateTime(new Date()));
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        activity.setEditBy(user.getId());

        ReturnObject returnObject = new ReturnObject();
        try {
            int num = activityService.saveUpdateActivity(activity);
            if (num != 1) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMassage("系统忙，修改失败，请重试");
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMassage("系统忙，修改失败，请重试");
        }

        return returnObject;
    }

    /**
     * 导出所有的市场活动以xls文件的格式
     * */
    @RequestMapping("/workbench/activity/exportToFileWithAllActivity.do")
    public void exportToFileWithAllActivity(HttpServletResponse response) throws IOException {

        List<Activity> activityList = activityService.queryAllActivitys();
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("市场活动列表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("活动id");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("活动名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述信息");
        cell = row.createCell(7);
        cell.setCellValue("创建时间");
        cell = row.createCell(8);
        cell.setCellValue("创建人");
        cell = row.createCell(9);
        cell.setCellValue("更改时间");
        cell = row.createCell(10);
        cell.setCellValue("更改人");

        if (activityList != null && activityList.size() > 0) {
            for (int i = 0; i < activityList.size(); i++) {
                Activity activity = activityList.get(i);
                row = sheet.createRow(i + 1);

                cell = row.createCell(0);
                cell.setCellValue(activity.getId());
                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell = row.createCell(2);
                cell.setCellValue(activity.getName());
                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell = row.createCell(7);
                cell.setCellValue(activity.getCreateBy());
                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateTime());
                cell = row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
        }
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=ActivityList.xls");
        OutputStream outputStream = response.getOutputStream();
        wb.write(outputStream);
        wb.close();
        outputStream.flush();

    }

    /**
     * 导出选中的市场活动信息到xls文件中
     * */
    @RequestMapping("workbench/activity/exportChangeActivity.do")
    public void exportChangeActivity(String[] id,HttpServletResponse response) throws IOException {
        List<Activity> activityList = activityService.exportChangeActivity(id);
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("市场活动列表-选择导出");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("活动id");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("活动名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("活动描述");
        cell = row.createCell(7);
        cell.setCellValue("创建时间");
        cell = row.createCell(8);
        cell.setCellValue("创建人");
        cell = row.createCell(9);
        cell.setCellValue("更改时间");
        cell = row.createCell(10);
        cell.setCellValue("更改人");

        for (int i = 0; i < activityList.size(); i++) {
            Activity activity = activityList.get(i);
            row = sheet.createRow(i + 1);
            cell = row.createCell(0);
            cell.setCellValue(activity.getId());
            cell = row.createCell(1);
            cell.setCellValue(activity.getOwner());
            cell = row.createCell(2);
            cell.setCellValue(activity.getName());
            cell = row.createCell(3);
            cell.setCellValue(activity.getStartDate());
            cell = row.createCell(4);
            cell.setCellValue(activity.getEndDate());
            cell = row.createCell(5);
            cell.setCellValue(activity.getCost());
            cell = row.createCell(6);
            cell.setCellValue(activity.getDescription());
            cell = row.createCell(7);
            cell.setCellValue(activity.getCreateTime());
            cell = row.createCell(8);
            cell.setCellValue(activity.getCreateBy());
            cell = row.createCell(9);
            cell.setCellValue(activity.getEditTime());
            cell = row.createCell(10);
            cell.setCellValue(activity.getEditBy());
        }

        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("content-disposition", "attachment;filename=ActivityLsitByChange.xls");
        OutputStream outputStream = response.getOutputStream();
        wb.write(outputStream);
        wb.close();
        outputStream.flush();
    }

    /**
    * 导入文件
    * */
    @RequestMapping("/workbench/activity/importActivityWithFile.do")
    @ResponseBody
    public Object importActivityWithFile(MultipartFile activityFile, HttpSession session) {

        User user = (User) session.getAttribute(Contants.SESSION_USER);
        ReturnObject returnObject = new ReturnObject();

        try {
            List<Activity> activityList = new ArrayList<>();
            InputStream inputStream = activityFile.getInputStream();

            /*activityFile.transferTo(new File("D:\\JavaProjects\\crm-SSM\\crm" + activityFile.getOriginalFilename()));
            InputStream in = new FileInputStream("D:\\JavaProjects\\crm-SSM\\crm" + activityFile.getOriginalFilename());*/
            HSSFWorkbook wb = new HSSFWorkbook(inputStream);
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row = null;
            HSSFCell cell = null;
            System.out.println(sheet.getLastRowNum()+"-------------=============================================================");
            a:for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);
                Activity activity = new Activity();
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(user.getId());
                activity.setCreateTime(DateUtils.formatDateTime(new Date()));
                activity.setCreateBy(user.getId());

                b:for (int j = 0; j < row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    if ("".equals(cell) || cell == null) {
                        continue a;//直接跳出外层循环，不再new新的对象，以免出现空数据
                    }
                    String str = "";
                    if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                        str = cell.getStringCellValue();
                    } else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                        str = cell.getNumericCellValue()+"";
                    } else if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
                        str = cell.getBooleanCellValue() + "";
                    } else if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
                        str = cell.getCellFormula();
                    } else {
                        str = "";
                    }

                    if (j == 0) {
                        activity.setName(str);
                    } else if (j == 1) {
                        activity.setStartDate(str);
                    } else if (j == 2) {
                        activity.setEndDate(str);
                    } else if (j == 3) {
                        str.substring(0, str.indexOf("."));
                        activity.setCost(str);
                    } else if (j == 4) {
                        activity.setDescription(str);
                    }
                }
                activityList.add(activity);
            }

            int saveNum = activityService.saveActivityFromFile(activityList);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setReturnData(saveNum);
        } catch (IOException e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMassage("系统正忙，请稍后重试");
        }

        return returnObject;
    }

    /**
     * 跳转到详情页
     */
    @RequestMapping("/workbench/activity/goActivityDetail.do")
    public String goActivityDetail(String id,HttpServletRequest request) {

        Activity activity = activityService.queryActivityDetailById(id);
        List<ActivityRemark> activityRemarkList = activityRemarkService.queryActivityRemarkByActivityId(id);

        request.setAttribute("activity", activity);
        request.setAttribute("activityRemarkList",activityRemarkList);

        System.out.println(activity);

        return "workbench/activity/detail";
    }

    //Test
    @RequestMapping("/workbench/activity/fileDownload.do")
    public void fileDownloadTest(HttpServletResponse response) throws Exception {


        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("我的表格");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("姓名");
        cell = row.createCell(2);
        cell.setCellValue("学号");
        cell = row.createCell(3);
        cell.setCellValue("年龄");
        cell = row.createCell(4);
        cell.setCellValue("性别");
        cell = row.createCell(5);
        cell.setCellValue("年级");

        for (int i = 0; i < 10; i++) {
            HSSFRow row1 = sheet.createRow(i + 1);
            HSSFCell cell1 = row1.createCell(0);
            cell1.setCellValue(i + 1);
            cell1 = row1.createCell(1);
            cell1.setCellValue("zhangsan");
            cell1 = row1.createCell(2);
            cell1.setCellValue(20220308);
            cell1 = row1.createCell(3);
            cell1.setCellValue(15);
            cell1 = row1.createCell(4);
            cell1.setCellValue("男");
            cell1 = row1.createCell(5);
            cell1.setCellValue("高一");
        }
       /*FileOutputStream out = new FileOutputStream("D:\\WorkSpeace\\xueshen.xls");
        wb.write(out);

        out.flush();
        out.close();*/
        //wb.close();

        response.setContentType("application/octet-stream;charset=UTF-8");
        //response.setContentType("application/x-xls;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=mystudent.xls");
        ServletOutputStream outputStream = response.getOutputStream();

       /* InputStream in = new FileInputStream("D:\\WorkSpeace\\xueshen.xls");

        byte[] bytes = new byte[256];
        int num = 0;
        while ((num=in.read(bytes)) != -1) {
            outputStream.write(bytes, 0, num);
        }

        in.close();*/
        wb.write(outputStream);
        wb.close();
        outputStream.flush();
    }
    //Test
    @RequestMapping("/workbench/activity/fileupload.do")
    @ResponseBody
    public Object fielUpload(String myname, MultipartFile myfile) throws IOException {

        System.out.println("获取的myname是：" + myname);
        System.out.println("原始的文件名是：" + myfile.getOriginalFilename());
        myfile.transferTo(new File("D:\\JavaProjects\\crm-SSM\\crm\\src\\main\\webapp" + myfile.getOriginalFilename()));

        ReturnObject object = new ReturnObject();
        object.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        object.setMassage("上传文件成功");

        HSSFWorkbook wb = new HSSFWorkbook();

        return object;
    }
}
