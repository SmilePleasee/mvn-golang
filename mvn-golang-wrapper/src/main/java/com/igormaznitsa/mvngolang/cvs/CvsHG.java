/*
 * Copyright 2016 Igor Maznitsa.
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

package com.igormaznitsa.mvngolang.cvs;

import com.igormaznitsa.mvngolang.utils.ProxySettings;
import java.io.File;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.maven.plugin.logging.Log;

class CvsHG extends AbstractRepo {

  CvsHG() {
    super("hg");
  }

  @Override
  public boolean doesContainCVS(@Nonnull final File folder) {
    return new File(folder, ".hg").isDirectory();
  }

  @Override
  public boolean processCVSRequisites(
      @Nonnull final Log logger,
      @Nullable final ProxySettings proxy,
      @Nullable final String customCommand,
      @Nonnull final File cvsFolder,
      @Nullable final String branchId,
      @Nullable final String tagId,
      @Nullable final String revisionId
  ) {
    boolean noError = true;

    if (branchId != null) {
      noError &= upToBranch(logger, proxy, customCommand, cvsFolder, branchId);
    }

    if (noError && tagId != null) {
      noError &= upToTag(logger, proxy, customCommand, cvsFolder, tagId);
    }

    if (noError && revisionId != null) {
      noError &= upToRevision(logger, proxy, customCommand, cvsFolder, revisionId);
    }

    return noError;
  }


  private boolean upToBranch(@Nonnull final Log logger, @Nullable final ProxySettings proxy,
                             @Nullable final String customCommand, @Nonnull final File cvsFolder,
                             @Nonnull final String branchId) {
    logger.debug("upToBranch: " + branchId);
    return checkResult(logger,
        execute(customCommand, logger, cvsFolder, "update", "--clean", branchId));
  }

  private boolean upToTag(@Nonnull final Log logger, @Nullable final ProxySettings proxy,
                          @Nullable final String customCommand, @Nonnull final File cvsFolder,
                          @Nonnull final String tagId) {
    logger.debug("upToTag: " + tagId);
    return checkResult(logger,
        execute(customCommand, logger, cvsFolder, "update", "--clean", tagId));
  }

  private boolean upToRevision(@Nonnull final Log logger, @Nullable final ProxySettings proxy,
                               @Nullable final String customCommand, @Nonnull final File cvsFolder,
                               @Nonnull final String revisionId) {
    logger.debug("upToRevision: " + revisionId);
    return checkResult(logger,
        execute(customCommand, logger, cvsFolder, "update", "--clean", "--rev", revisionId));
  }

}
