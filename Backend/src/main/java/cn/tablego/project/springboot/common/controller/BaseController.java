package cn.tablego.project.springboot.common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

/**
 * 用于其他Controller继承的基础Controller
 *
 * @author bianj
 * @version 1.0.0 2025-04-23
 */
public abstract class BaseController {
    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    @Autowired
    private Environment environment;
}
