import { Component, inject } from "@angular/core";
import { ArticlesService } from "../../services/articles.service";
import { CommonModule } from "@angular/common";
import { RouterLink } from "@angular/router";
import { tap } from "rxjs";
import { article } from "../../models/articlesModel";

@Component({
    selector: "app-articles",
    standalone: true,
    imports: [CommonModule, RouterLink],
    templateUrl: "./articles.component.html",
    styleUrl: "./articles.component.scss",
})
export class ArticlesComponent {
    private articleService = inject(ArticlesService);

    articles$ = this.articleService.getSubscribedArticles().pipe(
        tap((test) => {
            console.log(test);
        }),
    );

    sortByDateAsc = true;

    sortArticlesByDate(articles: article[]) {
        console.log(articles);
        if (this.sortByDateAsc) {
            return articles.sort(
                (a: article, b: article) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime(),
            );
        } else {
            return articles.sort(
                (a: article, b: article) => new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime(),
            );
        }
    }

    toggleSort() {
        this.sortByDateAsc = !this.sortByDateAsc;
    }
}
