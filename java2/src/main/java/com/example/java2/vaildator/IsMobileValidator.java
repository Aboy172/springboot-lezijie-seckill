//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.java2.vaildator;

import com.example.java2.utils.VaildatorUtil;
import org.thymeleaf.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {
    private boolean required = false;

    public IsMobileValidator() {
    }

    public void initialize(IsMobile constraintAnnotation) {
        boolean required = constraintAnnotation.required();
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (this.required) {
            return VaildatorUtil.isMobile(value);
        } else {
            return StringUtils.isEmpty(value) ? true : VaildatorUtil.isMobile(value);
        }
    }
}