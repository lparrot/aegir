<template>
  <div class="datatable">
    <div class="shadow border-b border-primary-200 sm:rounded-lg min-w-full overflow-auto">
      <table class="table-auto min-w-full divide-y divide-gray-200">
        <caption v-if="caption">{{ caption }}</caption>
        <thead class="bg-primary-200">
        <tr>
          <th v-for="field in fields" :key="`header-${field.key}`" :class="{'cursor-pointer hover:bg-primary-300': field.sortable == null || field.sortable}" class="py-3 px-3 text-left" scope="col" @click="onSort(field)">
            <div class="flex justify-between items-center">
              <slot :name="`header(${field.key})`">
                <span v-if="field.label">{{ field.label }}</span>
              </slot>

              <template v-if="sortField.field === field.key">
                <div>
                  <mdi-chevron-up v-if="sortField.asc" class="h-5 w-5"/>
                  <mdi-chevron-down v-else class="h-5 w-5"/>
                </div>
              </template>
            </div>
          </th>
        </tr>
        </thead>

        <tbody class="bg-white">
        <template v-if="items != null && items.length">
          <tr v-for="(item, itemIndex) in itemsShown" :key="`item-${get(item, idField)}`" :class="[{'hover:bg-primary-100 cursor-pointer': selectable, 'odd:bg-white even:bg-slate-50': striped}]" class="border-b border-b-primary-200" @click="onRowClick($event, item)">
            <td v-for="field in fields" :key="'field-' + field.key + '-'+itemIndex" class="py-2 px-3 whitespace-nowrap">
              <div :data-prevent-click="field.preventClick" class="item inline">
                <slot :field="field" :item="item" :name="`cell(${field.key})`" :value="getValue(field, item)">
                  <span>{{ getValue(field, item) }}</span>
                </slot>
              </div>
            </td>
          </tr>
        </template>
        <tr v-else>
          <td class="flex justify-center p-3">No content ...</td>
        </tr>
        </tbody>
      </table>
    </div>

    <div class="flex justify-end p-4">
      <Paginator v-model="pagination" @change="onFetch"/>
    </div>
  </div>
</template>

<script lang="ts" setup>
import get from "lodash/get";
import { nextTick, Ref } from "vue";

interface Props {
  caption?: string
  fetchUrl?: string
  fetchMethod?: AppFetchMethodType
  fields?: DatatableField[]
  idField?: string
  items?: any[],
  selectable?: boolean
  striped?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  idField: "id",
  fetchMethod: "get",
});

const emit = defineEmits<{
  (e: "row-click", item: any): void,
  (e: "update:items", payload: any): void
}>();

const { items } = useVModels(props, emit, { passive: true });

const sortField = ref({ asc: false, field: null });
const pagination: Ref<AppPagination> = ref({ page: 1 });

const itemsShown = computed(() => {
  if (props.fetchUrl != null) {
    return items.value;
  }

  return items.value.slice((pagination.value.page - 1) * pagination.value.size, pagination.value.size);
});

const onFetch = async () => {
  if (props.fetchUrl != null) {
    const response: any = await api.httpClient.axios["$" + props.fetchMethod](props.fetchUrl, {
      params: {
        size: pagination.value.size,
        page: pagination.value.page - 1,
        sort: pagination.value.order?.field,
      },
    });
    if (response.success) {
      if (response.result?.pageable != null) {
        items.value = response.result?.content;
        pagination.value.totalPage = response.result?.totalPages;
        pagination.value.count = response.result?.totalElements;
      } else {
        items.value = response.result;
      }
    }
  } else {
    pagination.value.totalPage = Math.ceil(items.value.length / pagination.value.size);
    pagination.value.count = items.value.length;
  }
};

nextTick(async () => {
  await onFetch();
});

const onRowClick = (event, item) => {
  const element: HTMLElement = event.target.closest(".item");
  if (element?.dataset?.preventClick) {
    event.preventDefault();
  } else {
    emit("row-click", item);
  }
};

const onSort = async (field: DatatableField) => {
  if (field.sortable == null || field.sortable) {
    if (sortField.value.field == null || sortField.value.field != field.key) {
      sortField.value.field = field.key;
      sortField.value.asc = true;
    } else {
      if (sortField.value.asc == null) {
        sortField.value.asc = true;
      } else if (sortField.value.asc) {
        sortField.value.asc = false;
      } else {
        sortField.value.field = null;
        sortField.value.asc = null;
      }
    }

    if (sortField.value.field != null) {
      pagination.value.order.field = sortField.value.field + (sortField.value.asc ? ",asc" : ",desc");
    } else {
      pagination.value.order = {};
    }

    await onFetch();
  }
};

const getValue = (field, item) => field.transform != null ? field.transform(get(item, field.field ?? field.key), item) : get(item, field.field ?? field.key);

defineExpose({
  refresh: onFetch,
});
</script>
