export class MFile {
  constructor(
    public name = "",
    public fullname = "",
    public parent = ""
  ) { }
}

export class FileInfo {
  constructor(
    public name: string = "",
    public fullname: string = "",
    public parent: string = "",
    public lastModifiedTime: string = "",
    public size: string = "",
    public owner: string = "",
    public fileType: string = "",
    public attrs: Array<string> = new Array(),
    public permissions: Array<string> = new Array()
  ) { }
}

export class FolderInfo {
  constructor(
    public folderCurrent: string = "",
    public folderParent: string = "",
    public files: Array<FileInfo> = new Array()
  ) { }
}
