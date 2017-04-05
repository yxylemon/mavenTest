package com.springtest.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.springtest.domain.UserInfo;
import com.springtest.service.IUserInfoService;
@Controller
public class POIController {
	@Resource
	private IUserInfoService userInfoService;
	
	
	@RequestMapping(value = "/user/down", method = RequestMethod.GET)
 	 @ResponseBody
   public String downPersons(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("进入到poi下载...");
		List<UserInfo> persons=userInfoService.selectAll();
       String []tableHeader={"用户id","用户名","密码"};
       short cellNumber=(short)tableHeader.length;//表的列数
		HSSFWorkbook workbook = new HSSFWorkbook();   //创建一个excel
		HSSFCell cell = null;                                    //Excel的列
		HSSFRow row = null;                                      //Excel的行
		HSSFCellStyle style = workbook.createCellStyle();        //设置表头的类型
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCellStyle style1 = workbook.createCellStyle();       //设置数据类型
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font = workbook.createFont();                 //设置字体
		HSSFSheet sheet = workbook.createSheet("sheet1");        //创建一个sheet
		HSSFHeader header = sheet.getHeader();//设置sheet的头
		
		if(persons.size()<1){
			header.setCenter("查无资料");
		}else{
			header.setCenter("用户下载测试表");
			row = sheet.createRow(0);
			row.setHeight((short)400);
			for(int k = 0;k < cellNumber;k++){
				cell = row.createCell(k);//创建第0行第k列
				cell.setCellValue(tableHeader[k]);//设置第0行第k列的值
				sheet.setColumnWidth(k,8000);//设置列的宽度
				font.setColor(HSSFFont.COLOR_NORMAL);      // 设置单元格字体的颜色.
				font.setFontHeight((short)350); //设置单元字体高度
				style1.setFont(font);//设置字体风格
				cell.setCellStyle(style1);
			}
			
			for(int i = 0 ;i < persons.size() ;i++){                            
				UserInfo person = persons.get(i);//获取student对象
				row = sheet.createRow((short) (i + 1));//创建第i+1行
				row.setHeight((short)400);//设置行高

				if(person.getUserid() != null){
					cell = row.createCell(0);//创建第i+1行第0列
					cell.setCellValue(person.getUserid());//设置第i+1行第0列的值
					cell.setCellStyle(style);//设置风格
				}
				if(person.getUsername() != null){
					cell = row.createCell(1); //创建第i+1行第1列

					cell.setCellValue(person.getUsername());//设置第i+1行第1列的值

					cell.setCellStyle(style); //设置风格
				}
				//由于下面的和上面的基本相同，就不加注释了
				if(person.getPassword() != null){
					cell = row.createCell(2);
					cell.setCellValue(person.getPassword());
					cell.setCellStyle(style);
				}
			}
		}
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			String headerStr ="用户下载测试表";
		 headerStr =new String(headerStr.getBytes("utf-8"), "ISO8859-1");//headerString为中文时转码
			 response.setHeader("Content-disposition","attachment; filename="+    headerStr+".xls");//filename是下载的xls的名，建议最好用英文
			 response.setContentType("application/msexcel;charset=UTF-8");//设置类型
		 response.setHeader("Pragma","No-cache");//设置头
			 response.setHeader("Cache-Control","no-cache");//设置头
			 response.setDateHeader("Expires", 0);//设置日期头
			workbook.write(out);
			out.flush();
			workbook.write(out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
       return null;
   }
 	
 	@RequestMapping(value = "/user/upLoad", method = RequestMethod.POST)
   public String upLoadTest( @RequestParam MultipartFile[]  myfiles,HttpServletRequest request) throws Exception {
    // 如果只是上传一个文件，则只需要MultipartFile类型接收文件即可，而且无需显式指定@RequestParam注解
       // 如果想上传多个文件，那么这里就要用MultipartFile[]类型来接收文件，并且还要指定@RequestParam注解
       // 并且上传多个文件时，前台表单中的所有<input type="file">的name都应该是myfiles，否则参数里的myfiles无法获取到所有上传的文件
       File[] files = new File[myfiles.length];
       for (MultipartFile myfile : myfiles) {
           if (myfile.isEmpty()) {
               System.out.println("文件未上传");
           } else {
               System.out.println("文件长度: " + myfile.getSize());
               System.out.println("文件类型: " + myfile.getContentType());
               System.out.println("文件名称: " + myfile.getName());
               System.out.println("文件原名: " + myfile.getOriginalFilename());
               System.out.println("========================================");
               // 如果用的是Tomcat服务器，则文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\WEB-INF\\upload\\文件夹中
               String realPath = request.getSession().getServletContext().getRealPath("/files/upload/loanData");
               // 这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉，我是看它的源码才知道的
               File file = new File(realPath, myfile.getOriginalFilename());
               FileUtils.copyInputStreamToFile(myfile.getInputStream(), file);
               if(myfile.getOriginalFilename().toLowerCase().endsWith("xls")){
                   readXls(myfile.getInputStream());
               }else{
                   readXlsx(file+"");
               }
           }
       }
       return "/success";
   }
 	
 	
 	 private void readXlsx(String fileName) throws IOException {
         //String fileName = "D:\\excel\\xlsx_test.xlsx";
         XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileName);
  
         // 循环工作表Sheet
         for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
             XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
             if (xssfSheet == null) {
                 continue;
             }
  
             // 循环行Row
             for (int rowNum = 0; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                 XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                 if (xssfRow == null) {
                     continue;
                 }
  
                 // 循环列Cell
                 for (int cellNum = 0; cellNum <= xssfRow.getLastCellNum(); cellNum++) {
                     XSSFCell xssfCell = xssfRow.getCell(cellNum);
                     if (xssfCell == null) {
                         continue;
                     }
                     System.out.print("   " + getValue(xssfCell));
                 }
                 System.out.println();
             }
         }
     }
              
     private String getValue(XSSFCell xssfCell) {
         if (xssfCell.getCellType() == xssfCell.CELL_TYPE_BOOLEAN) {
             return String.valueOf(xssfCell.getBooleanCellValue());
         } else if (xssfCell.getCellType() == xssfCell.CELL_TYPE_NUMERIC) {
             return String.valueOf(xssfCell.getNumericCellValue());
         } else {
             return String.valueOf(xssfCell.getStringCellValue());
         }
     }
      
     private void readXls(InputStream is) throws IOException {
         HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
  
         // 循环工作表Sheet
         for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
             HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
             if (hssfSheet == null) {
                 continue;
             }
  
             // 循环行Row
             for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                 HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                 UserInfo userTemp =new UserInfo();
                 if (hssfRow == null) {
                     continue;
                 }
  
                 // 循环列Cell
                 for (int cellNum = 0; cellNum <= hssfRow.getLastCellNum(); cellNum++) {
                     HSSFCell hssfCell = hssfRow.getCell(cellNum);
                     if (hssfCell == null) {
                         continue;
                     }
                     if(cellNum==0&&rowNum>0&&getValue(hssfCell)!=""){
                    	 int userId=(int) Double.parseDouble(getValue(hssfCell));
                    	 userTemp.setUserid(userId);
                     }
                     if(cellNum==1&&rowNum>0&&getValue(hssfCell)!=""){
                    	String userName= getValue(hssfCell);
                    	userTemp.setUsername(userName);
                     }
                     if(cellNum==2&&rowNum>0&&getValue(hssfCell)!=""){
                    	String passWord= getValue(hssfCell);
                    	userTemp.setPassword(passWord);
                    	
                     }
                     System.out.print("    " + getValue(hssfCell));
                 }
                 if(rowNum>0){
                	 userInfoService.insertSelective(userTemp);
                 }
                 System.out.println();
             }
         }
     }
  
     private String getValue(HSSFCell hssfCell) {
         if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
             return String.valueOf(hssfCell.getBooleanCellValue());
         } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
             return String.valueOf(hssfCell.getNumericCellValue());
         } else {
             return String.valueOf(hssfCell.getStringCellValue());
         }
     }
}
