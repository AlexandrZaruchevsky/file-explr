<template>
  <div 
    class="gap-2 flex w-full border-b px-1 py-0.5 items-center justify-start cursor-pointer hover:bg-stone-200"
    
  >
    <folder-icon class="p-1.5" v-if="file.fileType === 'FOLDER'" />
    <file-icon class="p-1.5" v-else />
    <span class="line-clamp-1 w-full">
      {{ file.name }}
    </span>
    <follow-folder-icon class="rounded p-1.5 hover:bg-stone-50 hover:bg-opacity-95"
      @click="emit('followFolderParent', file.parent)" />
  </div>
</template>

<script setup lang="ts">
import { FileInfo } from '@/model/FileModel';

withDefaults(defineProps<{
  file?: FileInfo
}>(), {
  file: () => new FileInfo()
})

const emit = defineEmits(['followFolderParent'])

</script>