package com.comaymanagement.cmd.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comaymanagement.cmd.constant.CrossOriginConstant;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = CrossOriginConstant.REACT_ORIGIN)
public class APIController {

}
