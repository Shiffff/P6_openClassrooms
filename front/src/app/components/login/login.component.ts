import { CommonModule } from "@angular/common";
import { Component, inject } from "@angular/core";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { Observable, catchError, finalize, map, tap, throwError } from "rxjs";
import { AuthentificationService } from "../../services/authentification.service";
import { Router, RouterLink } from "@angular/router";
import { authRes, loginInfo } from "../../models/authModel";

@Component({
    selector: "app-login",
    standalone: true,
    imports: [CommonModule, RouterLink, ReactiveFormsModule],
    templateUrl: "./login.component.html",
    styleUrl: "./login.component.scss",
})
export class LoginComponent {
    private authService = inject(AuthentificationService);
    private router = inject(Router);
    ErrorMsg: string | null = null;

    loading = false;
    requestRes$?: Observable<authRes>;

    emailOrUsername = new FormControl<string>("", [Validators.required]);
    password = new FormControl<string>("", [Validators.required]);

    form = new FormGroup({
        emailOrUsername: this.emailOrUsername,
        password: this.password,
    });

    onSubmitForm() {
        this.form.markAllAsTouched();
        this.ErrorMsg = null;
        const formvalue = this.form.value as loginInfo;
        if (!this.form.valid || !this.form.dirty || !formvalue) return;
        console.log(formvalue);
        this.loading = true;
        this.form.disable();
        this.requestRes$ = this.authService.login(formvalue).pipe(
            tap({
                error: (err) => {
                    this.ErrorMsg = err.error.code;
                },
            }),
            catchError(() => {
                this.ErrorMsg = "une erreur est suvernue";
                return throwError("Erreur serveur");
            }),
            map((res) => {
                this.authService.setToken(res.token);
                this.router.navigate(["/"]);
                return res;
            }),
            finalize(() => {
                this.loading = false;
                this.form.enable();
            }),
        );
    }
}
