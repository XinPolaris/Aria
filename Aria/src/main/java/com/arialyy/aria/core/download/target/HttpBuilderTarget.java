/*
 * Copyright (C) 2016 AriaLyy(https://github.com/AriaLyy/Aria)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.arialyy.aria.core.download.target;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import com.arialyy.aria.core.common.AbsBuilderTarget;
import com.arialyy.aria.core.common.HttpDelegate;
import com.arialyy.aria.core.download.m3u8.M3U8Delegate;
import com.arialyy.aria.core.processor.IHttpFileLenAdapter;
import com.arialyy.aria.core.inf.IOptionConstant;
import com.arialyy.aria.core.inf.Suggest;
import com.arialyy.aria.core.wrapper.ITaskWrapper;
import com.arialyy.aria.util.CheckUtil;

public class HttpBuilderTarget extends AbsBuilderTarget<HttpBuilderTarget> {

  private DNormalConfigHandler<HttpBuilderTarget> mConfigHandler;

  HttpBuilderTarget(String url) {
    mConfigHandler = new DNormalConfigHandler<>(this, -1);
    getTaskWrapper().setRequestType(ITaskWrapper.D_HTTP);
    mConfigHandler.setUrl(url);
  }

  @CheckResult(suggest = Suggest.TASK_CONTROLLER) public M3U8Delegate<HttpBuilderTarget> asM3U8() {
    return new M3U8Delegate<>(this, getTaskWrapper());
  }

  /**
   * 设置http请求参数，header等信息
   */
  @CheckResult(suggest = Suggest.TASK_CONTROLLER)
  public HttpDelegate<HttpBuilderTarget> option() {
    return new HttpDelegate<>(this, getTaskWrapper());
  }

  /**
   * 是否使用服务器通过content-disposition传递的文件名，内容格式{@code attachment;filename=***}
   * 如果获取不到服务器文件名，则使用用户设置的文件名
   *
   * @param use {@code true} 使用
   */
  @CheckResult(suggest = Suggest.TASK_CONTROLLER)
  public HttpBuilderTarget useServerFileName(boolean use) {
    getTaskWrapper().getOptionParams().setParams(IOptionConstant.useServerFileName, use);
    return this;
  }

  /**
   * 设置文件存储路径，如果需要修改新的文件名，修改路径便可。
   * 如：原文件路径 /mnt/sdcard/test.zip
   * 如果需要将test.zip改为game.zip，只需要重新设置文件路径为：/mnt/sdcard/game.zip
   *
   * @param filePath 路径必须为文件路径，不能为文件夹路径
   */
  @CheckResult(suggest = Suggest.TASK_CONTROLLER)
  public HttpBuilderTarget setFilePath(@NonNull String filePath) {
    mConfigHandler.setTempFilePath(filePath);
    return this;
  }

  /**
   * 设置文件存储路径，如果需要修改新的文件名，修改路径便可。
   * 如：原文件路径 /mnt/sdcard/test.zip
   * 如果需要将test.zip改为game.zip，只需要重新设置文件路径为：/mnt/sdcard/game.zip
   *
   * @param filePath 路径必须为文件路径，不能为文件夹路径
   * @param forceDownload {@code true}强制下载，不考虑文件路径是否被占用
   */
  @CheckResult(suggest = Suggest.TASK_CONTROLLER)
  public HttpBuilderTarget setFilePath(@NonNull String filePath, boolean forceDownload) {
    mConfigHandler.setTempFilePath(filePath);
    mConfigHandler.setForceDownload(forceDownload);
    return this;
  }

  /**
   * 如果你需要使用header中特定的key来设置文件长度，或有定制文件长度的需要，那么你可以通过该方法自行处理文件长度
   */
  @CheckResult(suggest = Suggest.TASK_CONTROLLER)
  public HttpBuilderTarget setFileLenAdapter(IHttpFileLenAdapter adapter) {
    if (adapter == null) {
      throw new IllegalArgumentException("adapter为空");
    }
    CheckUtil.checkMemberClass(adapter.getClass());
    getTaskWrapper().getOptionParams().setObjs(IOptionConstant.fileLenAdapter, adapter);
    return this;
  }
}