import { useAuthStore } from "@/stores/auth";

export default function useSecurity() {
  const authStore = useAuthStore();

  const isInRoles = (...roles: string[]) => {
    const userRoles = authStore.user?.roles as Array<string>;

    if (userRoles == null) {
      return false;
    }

    return roles.every(role => userRoles.indexOf(role) > -1);
  };

  return {
    isInRoles,
  };
}
