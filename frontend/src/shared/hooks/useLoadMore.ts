import { SelectItem } from '@/shared/components/common/LoadMoreSelect';
import { useInfiniteQuery } from '@tanstack/react-query';

interface UseLoadMoreOptions<T> {
  queryKey: string | readonly unknown[];
  fetchFn: (
    page: number,
    pageSize: number,
    searchQuery?: string,
    initialId?: string,
  ) => Promise<{
    data: T[];
    totalItems: number;
  }>;
  mapFn: (item: T) => SelectItem;
  initialItem?: SelectItem;
  initialPageSize?: number;
  searchQuery?: string;
  enabled?: boolean;
}

export function useLoadMore<T>({
  queryKey,
  fetchFn,
  mapFn,
  initialItem,
  initialPageSize = 5,
  searchQuery = '',
  enabled = true,
}: UseLoadMoreOptions<T>) {
  const infiniteQuery = useInfiniteQuery({
    queryKey: Array.isArray(queryKey)
      ? [...queryKey, searchQuery]
      : [queryKey, searchQuery],
    queryFn: async ({ pageParam = 1 }) => {
      return await fetchFn(pageParam, initialPageSize, searchQuery);
    },
    initialPageParam: 1,
    getNextPageParam: (lastPage, allPages) => {
      const totalFetched = allPages.reduce(
        (acc, page) => acc + page.data.length,
        0,
      );
      return totalFetched < lastPage.totalItems
        ? allPages.length + 1
        : undefined;
    },
    enabled,
  });

  // Flatten all pages of data
  const items = infiniteQuery.data?.pages.flatMap((page) => page.data) || [];

  // Map items to SelectItem format for use in dropdowns
  const mappedItems: SelectItem[] = items.map(mapFn);

  // Include initialItem at the beginning of the selectItems array if provided
  const selectItems: SelectItem[] = initialItem
    ? [initialItem, ...mappedItems]
    : mappedItems;

  // If duplicated items exist, filter it out
  const uniqueSelectItems = selectItems.filter(
    (item, index) =>
      selectItems.findIndex((i) => i.value === item.value) === index,
  );

  // Calculate if there are more items to load
  const hasMore = infiniteQuery.hasNextPage || false;

  return {
    items,
    selectItems: uniqueSelectItems,
    hasMore,
    isLoading: infiniteQuery.isLoading,
    isLoadingMore: infiniteQuery.isFetchingNextPage,
    error: infiniteQuery.error,
    loadMore: () => {
      if (hasMore && !infiniteQuery.isFetchingNextPage) {
        infiniteQuery.fetchNextPage();
      }
    },
    refresh: () => infiniteQuery.refetch(),
  };
}
