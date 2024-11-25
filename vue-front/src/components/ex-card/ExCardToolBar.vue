<template>
  <div class="w-full">
    <button class="btn-icon" @click="emit('changeFolderHome')">
      <home-icon />
    </button>
    <button class="btn-icon" @click="emit('changeFolderParent')">
      <undo-icon />
    </button>
    <div class="flex text-nowrap items-center font-bold justify-start px-2 w-full bg-stone-200 uppercase overflow-x-auto">
      <span class="">{{ folderCurrent === '' ? 'Home folder' : folderCurrent }}</span>
    </div>
    <button class="btn-icon" @click="emit('isFind', true)">
      <find-icon />
    </button>
  </div>
</template>

<script setup lang="ts">
const emit = defineEmits(['changeFolderHome', 'changeFolderParent', 'isFind'])
withDefaults(defineProps<{
  folderCurrent?: string
}>(), {
  folderCurrent: ''
})
</script>