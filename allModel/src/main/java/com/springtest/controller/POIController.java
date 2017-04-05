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
		System.out.println("���뵽poi����...");
		List<UserInfo> persons=userInfoService.selectAll();
       String []tableHeader={"�û�id","�û���","����"};
       short cellNumber=(short)tableHeader.length;//�������
		HSSFWorkbook workbook = new HSSFWorkbook();   //����һ��excel
		HSSFCell cell = null;                                    //Excel����
		HSSFRow row = null;                                      //Excel����
		HSSFCellStyle style = workbook.createCellStyle();        //���ñ�ͷ������
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCellStyle style1 = workbook.createCellStyle();       //������������
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font = workbook.createFont();                 //��������
		HSSFSheet sheet = workbook.createSheet("sheet1");        //����һ��sheet
		HSSFHeader header = sheet.getHeader();//����sheet��ͷ
		
		if(persons.size()<1){
			header.setCenter("��������");
		}else{
			header.setCenter("�û����ز��Ա�");
			row = sheet.createRow(0);
			row.setHeight((short)400);
			for(int k = 0;k < cellNumber;k++){
				cell = row.createCell(k);//������0�е�k��
				cell.setCellValue(tableHeader[k]);//���õ�0�е�k�е�ֵ
				sheet.setColumnWidth(k,8000);//�����еĿ��
				font.setColor(HSSFFont.COLOR_NORMAL);      // ���õ�Ԫ���������ɫ.
				font.setFontHeight((short)350); //���õ�Ԫ����߶�
				style1.setFont(font);//����������
				cell.setCellStyle(style1);
			}
			
			for(int i = 0 ;i < persons.size() ;i++){                            
				UserInfo person = persons.get(i);//��ȡstudent����
				row = sheet.createRow((short) (i + 1));//������i+1��
				row.setHeight((short)400);//�����и�

				if(person.getUserid() != null){
					cell = row.createCell(0);//������i+1�е�0��
					cell.setCellValue(person.getUserid());//���õ�i+1�е�0�е�ֵ
					cell.setCellStyle(style);//���÷��
				}
				if(person.getUsername() != null){
					cell = row.createCell(1); //������i+1�е�1��

					cell.setCellValue(person.getUsername());//���õ�i+1�е�1�е�ֵ

					cell.setCellStyle(style); //���÷��
				}
				//��������ĺ�����Ļ�����ͬ���Ͳ���ע����
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
			String headerStr ="�û����ز��Ա�";
		 headerStr =new String(headerStr.getBytes("utf-8"), "ISO8859-1");//headerStringΪ����ʱת��
			 response.setHeader("Content-disposition","attachment; filename="+    headerStr+".xls");//filename�����ص�xls���������������Ӣ��
			 response.setContentType("application/msexcel;charset=UTF-8");//��������
		 response.setHeader("Pragma","No-cache");//����ͷ
			 response.setHeader("Cache-Control","no-cache");//����ͷ
			 response.setDateHeader("Expires", 0);//��������ͷ
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
    // ���ֻ���ϴ�һ���ļ�����ֻ��ҪMultipartFile���ͽ����ļ����ɣ�����������ʽָ��@RequestParamע��
       // ������ϴ�����ļ�����ô�����Ҫ��MultipartFile[]�����������ļ������һ�Ҫָ��@RequestParamע��
       // �����ϴ�����ļ�ʱ��ǰ̨���е�����<input type="file">��name��Ӧ����myfiles������������myfiles�޷���ȡ�������ϴ����ļ�
       File[] files = new File[myfiles.length];
       for (MultipartFile myfile : myfiles) {
           if (myfile.isEmpty()) {
               System.out.println("�ļ�δ�ϴ�");
           } else {
               System.out.println("�ļ�����: " + myfile.getSize());
               System.out.println("�ļ�����: " + myfile.getContentType());
               System.out.println("�ļ�����: " + myfile.getName());
               System.out.println("�ļ�ԭ��: " + myfile.getOriginalFilename());
               System.out.println("========================================");
               // ����õ���Tomcat�����������ļ����ϴ���\\%TOMCAT_HOME%\\webapps\\YourWebProject\\WEB-INF\\upload\\�ļ�����
               String realPath = request.getSession().getServletContext().getRealPath("/files/upload/loanData");
               // ���ﲻ�ش���IO���رյ����⣬��ΪFileUtils.copyInputStreamToFile()�����ڲ����Զ����õ���IO���ص������ǿ�����Դ���֪����
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
  
         // ѭ��������Sheet
         for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
             XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
             if (xssfSheet == null) {
                 continue;
             }
  
             // ѭ����Row
             for (int rowNum = 0; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                 XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                 if (xssfRow == null) {
                     continue;
                 }
  
                 // ѭ����Cell
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
  
         // ѭ��������Sheet
         for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
             HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
             if (hssfSheet == null) {
                 continue;
             }
  
             // ѭ����Row
             for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                 HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                 UserInfo userTemp =new UserInfo();
                 if (hssfRow == null) {
                     continue;
                 }
  
                 // ѭ����Cell
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
