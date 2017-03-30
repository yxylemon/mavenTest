package com.springtest.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Controller
public class UPAndDownLoadController {
	private static Logger logger = LoggerFactory.getLogger(UPAndDownLoadController.class);
	/**
	 * spring�Դ����ļ��ϴ�
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	@RequestMapping("/springUpload")
	public String upLoad(HttpServletRequest request) throws Exception, IOException {
		long startTime = System.currentTimeMillis();
		// ����ǰ�����ĳ�ʼ���� CommonsMutipartResolver ���ಿ�ֽ�������
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// ���form���Ƿ���enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request)) {
			// ��request��ɶಿ��request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// ��ȡmultiRequest �����е��ļ���
			Iterator iter = multiRequest.getFileNames();

			while (iter.hasNext()) {
				// һ�α��������ļ�
				MultipartFile file = multiRequest.getFile(iter.next().toString());
				if (file != null) {
					String path = "E:/springUpload" + file.getOriginalFilename();
					// �ϴ�
					file.transferTo(new File(path));
				}

			}

		}
		long endTime = System.currentTimeMillis();
		System.out.println("������������ʱ�䣺" + String.valueOf(endTime - startTime) + "ms");
		return "/success";
	}

	@RequestMapping("/springDownload")
	public String downLoad(String fileName, HttpServletRequest request, HttpServletResponse response) {

		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
		try {
			String path = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "download";// ���downloadĿ¼Ϊɶ������classes�µ�
			InputStream inputStream = new FileInputStream(new File(path + File.separator + fileName));

			OutputStream os = response.getOutputStream();
			byte[] b = new byte[2048];
			int length;
			while ((length = inputStream.read(b)) > 0) {
				os.write(b, 0, length);
			}

			// ������Ҫ�رա�
			os.close();

			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ����ֵҪע�⣬Ҫ��Ȼ�ͳ�������������
		// java+getOutputStream() has already been called for this response
		return null;
	}
}
