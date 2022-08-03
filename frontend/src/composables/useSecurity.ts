import { useAuthStore } from "stores/auth";

const authStore = useAuthStore();

export class Security {
  isInRoles(roles: string[]) {
    return roles.every(role => authStore.user.roles.indexOf(role) > -1);
  }
}

const security = new Security();

export default function useSecurity() {
  const isInRoles = (roles: string[]) => {
    return security.isInRoles(roles);
  };

  return {
    isInRoles,
  };
}
