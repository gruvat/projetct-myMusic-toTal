package com.ciandt.summit.bootcamp2022.service;

import javax.servlet.http.HttpServletRequest;

public interface TokenService {
    boolean isAuthorized(HttpServletRequest request);
}
