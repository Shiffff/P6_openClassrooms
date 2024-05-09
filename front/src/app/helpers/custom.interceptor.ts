import { HttpErrorResponse, HttpInterceptorFn } from "@angular/common/http";
import { inject } from "@angular/core";
import { AuthentificationService } from "../services/authentification.service";
import { catchError, throwError } from "rxjs";

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
            if (err.status === 403) {
                authService.logout();
            }
            return throwError(err);
        }),
    );
};
