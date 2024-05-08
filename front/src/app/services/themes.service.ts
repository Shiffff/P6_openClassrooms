import { HttpClient } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";

@Injectable({
    providedIn: "root",
})
export class ThemesService {
    private http = inject(HttpClient);

    getSubscribedThemes() {
        return this.http.get<any>("/api/themes/subscribed");
    }
    getThemes() {
        return this.http.get<any>("/api/themes");
    }

    themeSubscribe(themeId: any) {
        return this.http.post<any>(`api/themes/${themeId}/subscribe`, null);
    }
    themeUnsubscribe(themeId: any) {
        return this.http.post<any>(`api/themes/${themeId}/unsubscribe`, null);
    }
}
