#!/usr/bin/env bash
# Runtime
mvn -f source/Zero.Core.Runtime.Metadata clean package install -Dquickly -DskipTests=true -Dmaven.javadoc.skip=true
# Depend On Runtime.Metadata
  mvn -f source/Zero.Core.Runtime.Assembly clean package install -Dquickly -DskipTests=true -Dmaven.javadoc.skip=true
  mvn -f source/Zero.Core.Runtime.Configuration clean package install -Dquickly -DskipTests=true -Dmaven.javadoc.skip=true
  mvn -f source/Zero.Core.Runtime.Domain clean package install -Dquickly -DskipTests=true -Dmaven.javadoc.skip=true
  mvn -f source/Zero.Core.Feature.Web.Cache clean package install -Dquickly -DskipTests=true -Dmaven.javadoc.skip=true
# Compare this snippet from zero-ws/zero-energy/mvn-release.sh: