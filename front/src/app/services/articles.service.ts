import { HttpClient } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";

@Injectable({
    providedIn: "root",
})
export class ArticlesService {
    private http = inject(HttpClient);

    getSubscribedArticles() {
        return this.http.get<any>("/api/themes/subscribed");
    }
}
