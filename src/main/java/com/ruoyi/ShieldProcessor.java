package com.ruoyi;

import javax.servlet.http.HttpServletRequest;


public interface ShieldProcessor {

    ShieldResponse process(HttpServletRequest request);
}
