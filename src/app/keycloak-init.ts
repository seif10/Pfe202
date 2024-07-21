// src/keycloak-init.ts
import { KeycloakService } from 'keycloak-angular';

export function initializeKeycloak(keycloak: KeycloakService) {
  return () => {
    if (typeof window !== 'undefined') {
      return keycloak.init({
        config: {
          url: 'http://localhost:8088', 
          realm: 'monitoringagent', 
          clientId: 'monitoringagent' 
        },
        initOptions: {
          onLoad: 'login-required', 
          checkLoginIframe: false
        }
      });
    } else {
      return Promise.resolve(); 
    }
  };
}
