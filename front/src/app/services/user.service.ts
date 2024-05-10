import { HttpClient } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { Observable } from "rxjs";
import { authRes, registerInfo } from "../models/authModel";

@Injectable({
    providedIn: "root",
})
export class UserService {
    private http = inject(HttpClient);

    userInfo(formValue: registerInfo): Observable<authRes> {
        return this.http.put<authRes>("/api/auth/me", formValue);
    }
}
