export interface AppError {
  status: number;
  message: string;
  path?: string;
  timestamp?: string;
}
