import { HttpClient } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";

@Injectable({
    providedIn: "root",
})
export class ArticlesService {
    private http = inject(HttpClient);

    getSubscribedArticles() {
        return this.http.get<any>("/api/articles/subscribed");
    }

    newArticle(article: any) {
        return this.http.post<any>("/api/article", article);
    }

    getArticle(articleId: any) {
        return this.http.get<any>(`api/article/${articleId}`);
    }
    addComment(comment: any) {
        return this.http.post<any>(`api/comment`, comment);
    }
}
