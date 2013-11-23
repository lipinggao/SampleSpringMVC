package com.test.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.test.model.TestViewModel;
import com.test.service.TestService;

@Controller
public class TestController {
	
	@Autowired
	TestService testService;

	@RequestMapping(value = "/welcome.html", method = RequestMethod.GET)
	public ModelAndView getLogin(HttpServletRequest request, HttpServletResponse response){
		ModelAndView model = new ModelAndView("welcome");
		TestViewModel vm = new TestViewModel();
		vm.setMsg(testService.getWelcomeMsg(request.getParameter("userId")));
		model.addObject("ViewModel", vm);
		return model;
	}
	
}
