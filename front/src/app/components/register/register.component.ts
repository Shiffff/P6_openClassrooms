import { CommonModule } from "@angular/common";
import { Component, inject } from "@angular/core";
import { AbstractControl, FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { Observable, catchError, finalize, map, tap, throwError } from "rxjs";
import { AuthentificationService } from "../../services/authentification.service";
import { Router, RouterLink } from "@angular/router";
import { authRes, registerInfo } from "../../models/authModel";

@Component({
    selector: "app-register",
    standalone: true,
    imports: [CommonModule, RouterLink, ReactiveFormsModule],
    templateUrl: "./register.component.html",
    styleUrl: "./register.component.scss",
})
export class RegisterComponent {
    private authService = inject(AuthentificationService);
    private router = inject(Router);
    ErrorMsg: string | null = null;

    loading = false;
    requestRes$?: Observable<authRes>;

    email = new FormControl<string>("", [Validators.required, Validators.email]);
    username = new FormControl<string>("", [Validators.required]);
    password = new FormControl<string>("", [Validators.required, this.passwordControl]);

    form = new FormGroup({
        email: this.email,
        username: this.username,
        password: this.password,
    });

    passwordControl(control: AbstractControl) {
        console.log(control);
        const passwordRegexp = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])[\w@#$%^&+=!]{8,}$/.test(
            control.value,
        );
        return passwordRegexp ? null : { passwordControl: true };
    }

    onSubmitForm() {
        this.form.markAllAsTouched();
        const formvalue = this.form.value as registerInfo;
        if (!this.form.valid || !this.form.dirty || !formvalue) return;
        this.ErrorMsg = null;
        this.loading = true;
        this.form.disable();
        this.requestRes$ = this.authService.register(formvalue).pipe(
            tap((data) => {
                console.log("err:", data);
            }),
            tap({
                error: (err) => {
                    if (err.error.message) {
                        this.ErrorMsg = err.error.message;
                    } else {
                        this.ErrorMsg = "une erreur est suvernue";
                    }
                },
            }),
            catchError(() => {
                this.ErrorMsg = "une erreur est suvernue";
                return throwError("Erreur serveur");
            }),
            map((res) => {
                console.log(res);
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
