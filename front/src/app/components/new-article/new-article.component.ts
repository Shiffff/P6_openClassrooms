import { CommonModule } from "@angular/common";
import { Component, inject } from "@angular/core";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { Observable, finalize, map, tap } from "rxjs";
import { AuthentificationService } from "../../services/authentification.service";
import { Router, RouterLink } from "@angular/router";
import { ArticlesService } from "../../services/articles.service";
import { ThemesService } from "../../services/themes.service";

@Component({
    selector: "app-new-article",
    standalone: true,
    imports: [CommonModule, RouterLink, ReactiveFormsModule],
    templateUrl: "./new-article.component.html",
    styleUrl: "./new-article.component.scss",
})
export class NewArticleComponent {
    private articleService = inject(ArticlesService);
    private themesService = inject(ThemesService);
    private router = inject(Router);
    ErrorMsg!: number | null;

    loading = false;
    requestRes$?: Observable<any>;
    themes$ = this.themesService.getThemes();

    themeId = new FormControl<string>("", [Validators.required]);
    title = new FormControl<string>("", [Validators.required]);
    content = new FormControl<string>("", [Validators.required]);

    form = new FormGroup({
        themeId: this.themeId,
        title: this.title,
        content: this.content,
    });

    onSubmitForm() {
        this.ErrorMsg = null;
        this.form.markAsTouched();
        const formvalue = this.form.value;
        console.log(formvalue);
        this.loading = true;
        this.form.disable();
        this.requestRes$ = this.articleService.newArticle(formvalue).pipe(
            tap({
                error: (err) => {
                    this.ErrorMsg = err.error.code;
                },
            }),
            map((res) => {
                this.router.navigate(["/"]);
            }),
            finalize(() => {
                this.loading = false;
                this.form.enable();
            }),
        );
    }
}
