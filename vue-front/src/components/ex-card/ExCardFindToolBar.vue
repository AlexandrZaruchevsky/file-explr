<template>
  <form class="ex-card-tool-bar" @submit.prevent="emit('find', filter)">
    <input ref="filterFocus" type="text" v-model="filter" />
    <button class="btn-icon">
      <find-icon />
    </button>
  </form>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
const filterFocus = ref<HTMLInputElement | null>(null)

const filter = ref<string>("")
const emit = defineEmits(['find'])

onMounted(() => {
  setTimeout(()=>{
    if (filterFocus.value){
      filterFocus.value.focus()
    }
  }, 50);
})

</script>

<style lang="scss" scoped>
.ex-card-tool-bar {
    @apply flex w-full p-1 gap-2 bg-stone-50;

    input {
      @apply w-full px-1.5 py-0.5;
      @apply bg-stone-50;
      @apply border-b-2 border-stone-300;

      &:focus {
        @apply outline-none;
        @apply border-stone-500;
      }
    }

    .btn {
      @apply px-1.5 py-0.5;
      @apply bg-stone-50 rounded;
      @apply border-2 border-stone-300;

      &:hover {
        @apply border-stone-400;
      }

      &:active {
        @apply shadow-md;
      }
    }
  }
</style>