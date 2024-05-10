import { HttpClient } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { Router } from "@angular/router";
import { BehaviorSubject, Observable, tap } from "rxjs";
import { registerInfo, loginInfo, authRes, userInfo } from "../models/authModel";

@Injectable({
    providedIn: "root",
})
export class AuthentificationService {
    private isLoggedSubject: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

    private http = inject(HttpClient);
    private router = inject(Router);
    constructor() {
        this.isLoggedSubject.next(this.getToken() !== null);
    }

    login(formValue: loginInfo): Observable<authRes> {
        return this.http.post<authRes>("/api/auth/login", formValue);
    }

    register(formValue: registerInfo): Observable<authRes> {
        console.log(formValue);

        return this.http.post<authRes>("/api/auth/register", formValue);
    }
    getToken() {
        return localStorage.getItem("mddToken");
    }
    logout() {
        let removeToken = localStorage.removeItem("mddToken");
        if (removeToken == null) {
            this.router.navigate(["/"]);
        }
        this.isLoggedSubject.next(false);
    }
    setToken(token: string) {
        localStorage.setItem("mddToken", token);
        this.isLoggedSubject.next(true);
    }
    isLoggedIn(): Observable<boolean> {
        this.isLoggedSubject.next(this.getToken() !== null);
        return this.isLoggedSubject.asObservable();
    }

    getUserInfo() {
        return this.http.get<userInfo>("/api/auth/me");
    }
}
