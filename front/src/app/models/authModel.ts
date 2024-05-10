export interface authRes {
    token: string;
}
export interface registerInfo {
    email: string;
    password?: string;
    username: string;
}
export interface loginInfo {
    emailOrUsername: string;
    password: string;
}
export interface userInfo {
    created_at: string;
    email: string;
    username: string;
}
