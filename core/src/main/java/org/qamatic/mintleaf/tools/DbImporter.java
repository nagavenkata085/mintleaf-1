/*
 *
 *  *
 *  *  * <!--
 *  *  *   ~
 *  *  *   ~ The MIT License (MIT)
 *  *  *   ~
 *  *  *   ~ Copyright (c) 2010-2017 QAMatic
 *  *  *   ~
 *  *  *   ~ Permission is hereby granted, free of charge, to any person obtaining a copy
 *  *  *   ~ of this software and associated documentation files (the "Software"), to deal
 *  *  *   ~ in the Software without restriction, including without limitation the rights
 *  *  *   ~ to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  *  *   ~ copies of the Software, and to permit persons to whom the Software is
 *  *  *   ~ furnished to do so, subject to the following conditions:
 *  *  *   ~
 *  *  *   ~ The above copyright notice and this permission notice shall be included in all
 *  *  *   ~ copies or substantial portions of the Software.
 *  *  *   ~
 *  *  *   ~ THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  *  *   ~ IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  *  *   ~ FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  *  *   ~ AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  *  *   ~ LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  *  *   ~ OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  *  *   ~ SOFTWARE.
 *  *  *   ~
 *  *  *   ~
 *  *  *   -->
 *  *
 *  *
 *
 */

package org.qamatic.mintleaf.tools;

import org.qamatic.mintleaf.DataAction;
import org.qamatic.mintleaf.DriverSource;
import org.qamatic.mintleaf.MintLeafException;
import org.qamatic.mintleaf.MintLeafLogger;
import org.qamatic.mintleaf.core.FluentJdbc;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by qamatic on 3/6/16.
 */
public class DbImporter extends ImpExpBase implements DataAction {
    private static final MintLeafLogger logger = MintLeafLogger.getLogger(DbImporter.class);

    private DriverSource targetDbDriverSource;
    private String targetSqlTemplate;
    private DriverSource sourceDbDriverSource;
    private String sourceSql;
    private Object[] sourceSqlParamValueBindings;

    public DbImporter(DriverSource sourceDbDriverSource, String sourceSql,
                      DriverSource targetDbDriverSource,
                      String targetSqlTemplate) {
        this.sourceDbDriverSource = sourceDbDriverSource;
        this.sourceSql = sourceSql;
        this.targetDbDriverSource = targetDbDriverSource;
        this.targetSqlTemplate = targetSqlTemplate;
    }

    @Override
    public void execute() throws MintLeafException {

        try {
            FluentJdbc sourceDbQuery = this.sourceDbDriverSource.queryBuilder().
                    withSql(sourceSql).
                    withParamValues(this.sourceSqlParamValueBindings);

            importDataFrom(new DbImportFlavour(sourceDbQuery), this.targetSqlTemplate);

        } catch (SQLException e) {
            throw new MintLeafException(e);
        } catch (IOException e) {
            throw new MintLeafException(e);
        }
    }

    @Override
    protected DriverSource getDriverSource() {
        return this.sourceDbDriverSource;
    }

    public void setSourceSqlParamValueBindings(Object[] sourceSqlParamValueBindings) {
        this.sourceSqlParamValueBindings = sourceSqlParamValueBindings;
    }
}
