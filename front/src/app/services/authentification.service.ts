import { HttpClient } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { Router } from "@angular/router";
import { BehaviorSubject, Observable } from "rxjs";

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

    login(formValue: any): Observable<any> {
        return this.http.post<any>("/api/auth/login", formValue);
    }
    register(formValue: any): Observable<any> {
        return this.http.post<any>("/api/auth/register", formValue);
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
}
