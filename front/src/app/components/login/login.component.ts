import { CommonModule } from "@angular/common";
import { Component, inject } from "@angular/core";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { Observable, finalize, map, tap } from "rxjs";
import { AuthentificationService } from "../../services/authentification.service";
import { Router, RouterLink } from "@angular/router";

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
    ErrorMsg!: number | null;

    loading = false;
    requestRes$?: Observable<any>;

    emailOrUsername = new FormControl<string>("testdadzdagzdedazd121@test.fr", [Validators.required]);
    password = new FormControl<string>("Teszfzefzefze1cvdsvs3!", [Validators.required]);

    form = new FormGroup({
        emailOrUsername: this.emailOrUsername,
        password: this.password,
    });

    onSubmitForm() {
        this.ErrorMsg = null;
        this.form.markAsTouched();
        const formvalue = this.form.value;
        console.log(formvalue);
        this.loading = true;
        this.form.disable();
        this.requestRes$ = this.authService.login(formvalue).pipe(
            tap({
                error: (err) => {
                    this.ErrorMsg = err.error.code;
                },
            }),
            map((res) => {
                this.authService.setToken(res.token);
                this.router.navigate(["/"]);
            }),
            finalize(() => {
                this.loading = false;
                this.form.enable();
            }),
        );
    }
}
