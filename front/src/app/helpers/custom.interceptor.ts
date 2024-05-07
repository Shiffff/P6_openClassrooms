import { HttpErrorResponse, HttpInterceptorFn } from "@angular/common/http";
import { inject } from "@angular/core";
import { AuthentificationService } from "../services/authentification.service";
import { catchError } from "rxjs";

export const customInterceptor: HttpInterceptorFn = (req, next) => {
    const authService = inject(AuthentificationService);
    const token = authService.getToken();

    const cloneRequest = req.clone({
        setHeaders: {
            Authorization: `Bearer ${token}`,
        },
    });

    return next(cloneRequest).pipe(
        catchError((err: HttpErrorResponse) => {
            console.log(err);
            throw err;
        }),
    );
};
