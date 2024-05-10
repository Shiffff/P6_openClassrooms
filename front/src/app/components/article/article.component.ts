import { Component, inject } from "@angular/core";
import { ActivatedRoute, RouterLink } from "@angular/router";
import { ArticlesService } from "../../services/articles.service";
import { CommonModule } from "@angular/common";
import { Observable, finalize, tap } from "rxjs";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { article, newComment } from "../../models/articlesModel";

@Component({
    selector: "app-article",
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, RouterLink],
    templateUrl: "./article.component.html",
    styleUrl: "./article.component.scss",
})
export class ArticleComponent {
    private articlesService = inject(ArticlesService);
    private route = inject(ActivatedRoute);
    article$!: Observable<article>;
    ErrorMsg!: number | null;

    loading = false;

    articleId!: string;

    ngOnInit(): void {
        this.route.params.subscribe((params) => {
            this.articleId = params["id"];
        });
        this.article$ = this.articlesService.getArticle(this.articleId);
    }

    comment = new FormControl<string>("", [Validators.required]);

    form = new FormGroup({
        content: this.comment,
    });

    onSubmitForm() {
        this.ErrorMsg = null;
        this.form.markAsTouched();
        const formvalue = {
            ...this.form.value,
            articleId: this.articleId,
        } as newComment;
        this.loading = true;
        this.form.disable();
        this.articlesService
            .addComment(formvalue)
            .pipe(
                tap(() => {
                    this.article$ = this.articlesService.getArticle(this.articleId);
                }),
                finalize(() => {
                    this.form.enable();
                    this.form.reset();
                }),
            )
            .subscribe();
    }
}
