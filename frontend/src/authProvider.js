import { MsalAuthProvider, LoginType } from 'react-aad-msal';
 
// Msal Configurations
const config = {
  auth: {
    authority: "https://login.microsoftonline.com/89c8e12f-63cd-4da6-a5e0-d4aa2457f40e",
    clientId: "9c2fd2b4-0994-4b40-9b88-2cee07b36b51",
    redirectUri: window.location.origin,
    postLogoutRedirectUri: window.location.origin,
    validateAuthority: true,
    navigateToLoginRequestUrl: true
  },
  cache: {
    cacheLocation: "localStorage",
    storeAuthStateInCookie: true
  }
};
 
// Authentication Parameters
const authenticationParameters = {
  scopes: ["user.read"]
}
 
// Options
const options = {
  loginType: LoginType.Popup,
  tokenRefreshUri: window.location.origin + '/auth.html'
}
 
export const authProvider = new MsalAuthProvider(config, authenticationParameters, options)