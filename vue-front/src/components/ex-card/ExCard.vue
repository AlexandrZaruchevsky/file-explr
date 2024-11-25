<template>
  <div class="ex-card">
    <div class="ex-card-tool-bar">
      <slot name="tool-bar" />
    </div>
    <div class="ex-card-body">
      <div class="overflow-auto">
        <div v-for="file in files" :key="file.fullname">
          <slot name="row" :file="file"></slot>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { FileInfo } from '@/model/FileModel';


const emit = defineEmits(['changeFolderHome', 'changeParentFolder', 'isFind'])

withDefaults(defineProps<{
  files?: Array<FileInfo>
}>(), {
  files: () => []
})

</script>

<style lang="scss" scoped>
.ex-card {
  @apply w-full sm:w-5/6 md:w-4/6 lg:w-3/6 xl:w-2/6;
  @apply overflow-auto;
  @apply bg-stone-200;
  @apply flex flex-col;
  @apply shadow-xl shadow-stone-600;

  .ex-card-tool-bar {
    @apply flex w-full p-1 gap-2 bg-stone-50;
  }

  .ex-card-body {
    @apply flex flex-col h-full w-full overflow-auto;
    @apply bg-white p-2;
  }
}
</style>