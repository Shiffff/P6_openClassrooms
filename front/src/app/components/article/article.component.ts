import { Component, inject } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { ArticlesService } from "../../services/articles.service";
import { CommonModule } from "@angular/common";
import { Observable, tap } from "rxjs";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";

@Component({
    selector: "app-article",
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    templateUrl: "./article.component.html",
    styleUrl: "./article.component.scss",
})
export class ArticleComponent {
    private articlesService = inject(ArticlesService);
    private route = inject(ActivatedRoute);
    article$!: Observable<any>;
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
        const formvalue: any = {
            ...this.form.value,
            articleId: this.articleId,
        };
        this.loading = true;
        this.form.disable();
        this.articlesService
            .addComment(formvalue)
            .pipe(
                tap(() => {
                    this.article$ = this.articlesService.getArticle(this.articleId);
                }),
            )
            .subscribe();
    }
}
