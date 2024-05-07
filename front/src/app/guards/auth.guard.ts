import { inject } from "@angular/core";
import { CanActivateFn, Router } from "@angular/router";
import { AuthentificationService } from "../services/authentification.service";
import { map } from "rxjs";

export const authGuard: CanActivateFn = (route, state) => {
    const authService = inject(AuthentificationService);
    const router = inject(Router);

    return authService.isLoggedIn().pipe(
        map((isLogged) => {
            if (!isLogged) {
                router.navigate(["/login"]);
                return false;
            }
            return true;
        }),
    );
};
