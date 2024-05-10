import { CommonModule } from "@angular/common";
import { Component, inject } from "@angular/core";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { Router, RouterLink } from "@angular/router";
import { AuthentificationService } from "../../services/authentification.service";
import { Observable, finalize, map, tap } from "rxjs";
import { UserService } from "../../services/user.service";
import { ThemesService } from "../../services/themes.service";
import { authRes, registerInfo } from "../../models/authModel";

@Component({
    selector: "app-profil",
    standalone: true,
    imports: [CommonModule, RouterLink, ReactiveFormsModule],
    templateUrl: "./profil.component.html",
    styleUrl: "./profil.component.scss",
})
export class ProfilComponent {
    private authService = inject(AuthentificationService);
    private userService = inject(UserService);
    private themesService = inject(ThemesService);
    ErrorMsg!: number | null;
    requestRes$?: Observable<authRes>;

    userInfo$ = this.authService.getUserInfo();
    themes$ = this.themesService.getSubscribedThemes();

    loading = false;

    email = new FormControl<string>("", [Validators.required]);
    username = new FormControl<string>("", [Validators.required]);

    form = new FormGroup({
        email: this.email,
        username: this.username,
    });
    putUserInfo() {
        this.ErrorMsg = null;
        this.form.markAsTouched();
        const formvalue = this.form.value as registerInfo;
        console.log(formvalue);
        this.loading = true;
        this.form.disable();
        this.requestRes$ = this.userService.userInfo(formvalue).pipe(
            tap((test) => {
                console.log(test);
            }),
            tap({
                error: (err) => {
                    this.ErrorMsg = err.error.code;
                },
            }),
            map((res) => {
                localStorage.setItem("mddToken", res.token);
                return res;
            }),
            finalize(() => {
                this.loading = false;
                this.form.enable();
            }),
        );
    }
    subscribe(themeId: number) {
        this.themesService
            .themeUnsubscribe(themeId)
            .pipe(
                tap(() => {
                    this.themes$ = this.themesService.getSubscribedThemes();
                }),
            )
            .subscribe();
    }
    logout() {
        this.authService.logout();
    }
}
