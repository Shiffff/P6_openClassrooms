import { CommonModule } from "@angular/common";
import { Component, inject } from "@angular/core";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { Observable, finalize, map, tap } from "rxjs";
import { AuthentificationService } from "../../services/authentification.service";
import { Router, RouterLink } from "@angular/router";

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
    ErrorMsg!: number | null;

    loading = false;
    requestRes$?: Observable<any>;

    email = new FormControl<string>("", [Validators.required]);
    username = new FormControl<string>("", [Validators.required]);
    password = new FormControl<string>("", [Validators.required]);

    form = new FormGroup({
        email: this.email,
        username: this.username,
        password: this.password,
    });

    onSubmitForm() {
        this.ErrorMsg = null;
        this.form.markAsTouched();
        const formvalue = this.form.value;
        console.log(formvalue);
        this.loading = true;
        this.form.disable();
        this.requestRes$ = this.authService.register(formvalue).pipe(
            tap({
                error: (err) => {
                    this.ErrorMsg = err.error.code;
                },
            }),
            map((res) => {
                console.log(res);
                localStorage.setItem("mddToken", res.token);
                this.router.navigate(["/articles"]);
            }),
            finalize(() => {
                this.loading = false;
                this.form.enable();
            }),
        );
    }
}
