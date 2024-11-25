<template>
  <div id="explr-app">
    <ex-card class="h-full" :files="files">
      <template v-slot:tool-bar>
        <ex-card-tool-bar :folder-current="folderCurrent" class="flex gap-1" @change-folder-home="getFolderInfo"
          @change-folder-parent="getFolderInfo(folderParent)" @is-find="find" />
      </template>
      <template v-slot:row="slotProps">
        <ex-card-row :file="slotProps.file" @change-folder="getFolderInfo" @delete-item="deleteItem" />
      </template>
    </ex-card>
    <div class="ex-overflow" @click.stop="isFilter = false" v-if="isFilter">
      <ex-card @click.stop :files="fileInfoFilter" class="rounded" style="min-height: 100px;">
        <template v-slot:tool-bar>
          <ex-card-find-tool-bar @find="findFileInfo" />
        </template>
        <template v-slot:row="slotProps">
          <ex-card-find-row :file="slotProps.file" @follow-folder-parent="followFolderParent" />
        </template>
      </ex-card>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { computed, onMounted, ref } from 'vue';
import { FileInfo, FolderInfo } from './model/FileModel';

import ExCard from '@/components/ex-card/ExCard.vue'
import ExCardToolBar from '@/components/ex-card/ExCardToolBar.vue';
import ExCardRow from '@/components/ex-card/ExCardRow.vue';
import ExCardFindToolBar from '@/components/ex-card/ExCardFindToolBar.vue';
import ExCardFindRow from '@/components/ex-card/ExCardFindRow.vue';

const restApi = import.meta.env.VITE_REST_API

const regex = /\\/g

const folderInfo = ref<FolderInfo>(new FolderInfo())
const folderCurrent = computed((): string => folderInfo.value.folderCurrent)
const folderParent = computed((): string => folderInfo.value.folderParent)
const files = computed((): Array<FileInfo> => folderInfo.value.files)

const fileInfoFilter = ref<Array<FileInfo>>([])
const isFilter = ref<boolean>(false)

const find = (isFind: boolean = false) => {
  isFilter.value = isFind
  fileInfoFilter.value = []
}
const getFolderInfo = async (folder: string | FileInfo | undefined = undefined) => {
  if (folder) {
    if (typeof folder === "string") {
      folderInfo.value = await (await fetch(`${restApi}files?folder=${folder.replace(regex, '/')}`)).json()
    }
    else if (folder.fileType === "FOLDER") {
      folderInfo.value = await (await fetch(`${restApi}files?folder=${folder.fullname.replace(regex, '/')}`)).json()
    }
  } else {
    folderInfo.value = await (await fetch(`${restApi}files`)).json()
  }
}

const findFileInfo = async (filter: string = ""): Promise<void> => {
  if (filter !== "") {
    const params = new URLSearchParams();
    params.append("filter", filter.replace(regex, '/'));
    fileInfoFilter.value = await (await fetch(`${restApi}files/filter?${params}`)).json()
  }
}

const followFolderParent = async (folder: string): Promise<void> => {
  await getFolderInfo(folder)
  isFilter.value = false
}

const deleteItem = async (file: string | FileInfo = ''): Promise<void> => {
  let body = {}
  if (typeof file === 'string') {
    body = {filename: file.replace(regex,"/")}
  } else {
    body = {filename: file.fullname.replace(regex,"/")}
  }
  const response =await fetch(`${restApi}files/delete`, {
    method: 'POST',
    headers: {
      'Content-type': 'application/json; charset=UTF-8' 
    },
    body: JSON.stringify({
      ...body
    })
  })

  response.json().then(async r => {
    if (r) {
      await getFolderInfo(folderCurrent.value);
    }
  })

}

onMounted(async () => {
  await getFolderInfo();
})

</script>

<style lang="scss" scoped>
#explr-app {
  @apply flex flex-col h-full w-full justify-center items-center;
  @apply bg-stone-600 overflow-auto;
  @apply relative;
}

.ex-overflow {
  @apply bg-stone-500 bg-opacity-50;
  @apply absolute;
  @apply flex flex-col h-full w-full justify-start items-center;
  @apply px-8 py-4;
  @apply opacity-95;
}
</style>
