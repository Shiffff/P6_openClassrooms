import { Component, inject } from "@angular/core";
import { ArticlesService } from "../../services/articles.service";
import { CommonModule } from "@angular/common";
import { RouterLink } from "@angular/router";

@Component({
    selector: "app-articles",
    standalone: true,
    imports: [CommonModule, RouterLink],
    templateUrl: "./articles.component.html",
    styleUrl: "./articles.component.scss",
})
export class ArticlesComponent {
    private articleService = inject(ArticlesService);

    articles$ = this.articleService.getSubscribedArticles();
}
