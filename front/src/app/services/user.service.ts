import { HttpClient } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { Observable } from "rxjs";

@Injectable({
    providedIn: "root",
})
export class UserService {
    private http = inject(HttpClient);

    userInfo(formValue: any): Observable<any> {
        return this.http.put<any>("/api/auth/me", formValue);
    }
}
