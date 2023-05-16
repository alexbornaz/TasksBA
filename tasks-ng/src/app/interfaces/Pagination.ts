export interface Pagination<T> {
  items: T[];
  totalPages: number;
  currentPage: number;
}
