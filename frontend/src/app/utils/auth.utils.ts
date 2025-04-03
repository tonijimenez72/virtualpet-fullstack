// src/app/utils/auth.utils.ts

export function decodeToken(): any | null {
  const token = localStorage.getItem('authToken');
  if (!token) return null;

  try {
    const payload = token.split('.')[1];
    return JSON.parse(atob(payload));
  } catch (error) {
    console.error('❌ Failed to decode token:', error);
    return null;
  }
}
