<template>
  <div 
    class="gap-2 flex w-full border-b px-1 py-0.5 items-center justify-start cursor-pointer bg-stone-600 bg-opacity-0 hover:bg-opacity-5"
     @click="emit('changeFolder', file)"
  >
    <folder-icon class="p-1.5" v-if="file.fileType === 'FOLDER'" />
    <file-icon class="p-1.5" v-else />
    <span class="line-clamp-1 w-full">
      {{ file.name }}
    </span>
    <button class="btn-icon" @click.stop="emit('deleteItem', file)">
      <delete-icon />
    </button>
  </div>
</template>

<script setup lang="ts">
import { FileInfo } from '@/model/FileModel';

withDefaults(defineProps<{
  file?: FileInfo
}>(), {
  file: () => new FileInfo()
})

const emit = defineEmits(['changeFolder', 'deleteItem'])

</script>