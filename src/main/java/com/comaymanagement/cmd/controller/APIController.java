package com.comaymanagement.cmd.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.comaymanagement.cmd.constant.CrossOriginConstant;

@RestController
@RequestMapping("/API")
@CrossOrigin(origins = CrossOriginConstant.REACT_ORIGIN)
public class APIController {
	@Autowired
	ServletContext context;
	@PostMapping("/UploadFile")
	@ResponseBody
	public String uploadFile(MultipartHttpServletRequest request) {
//		// Lay r ds ten file
			MultiValueMap<String, MultipartFile>  form = request.getMultiFileMap();
			
			List<MultipartFile> files = form.get("file");
			String pathSaveFile = "";
			for(MultipartFile mpf : files) {
				if( mpf.getOriginalFilename().equals("")) {
					continue;
				}
//			B1: lay ra duong dan se luu file
//				C:\Practive\Source\CMD-FE\build
				pathSaveFile = "C:\\Practive\\Source\\CMD-FE\\build\\";
//			B2: Tao file
//				String name = String.format("%s%s", RandomStringUtils.randomAlphanumeric(10), mpf.getOriginalFilename());
//				File file = new File(pathSaveFile + name);
				File file = new File(pathSaveFile + mpf.getOriginalFilename());
//			B3: dung ham trong thu vien commmon de save
				try {
					mpf.transferTo(file);
				} catch (IOException e) {
					System.err.println(e.getStackTrace());
				}
			}
			
			System.out.println(pathSaveFile);
			return "upload thanh cong";
	}
}
