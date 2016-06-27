/*
 * Copyright (c) 2016. Cloves Almeida. All rights reserved.
 */

package tt.calories.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception to 401 UNAUTHORIZED.
 *
 * @author Cloves J. G. Almeida <cjalmeida@gmail.com>
 * @version 1.0.0
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {

}
