DESCRIPTION = "Build Caffe library for CNN using OpenBLAS lib"
AUTHOR = "Alexander Leiva <norxander@gmail.com>"
SUMMARY = "Caffe : A fast open framework for deep learning"
HOMEPAGE = "http://caffe.berkeleyvision.org/"
LICENSE = "BSD"
PRIORITY= "optional"
SECTION = "libs"
PR = "r0"

DEPENDS = " \
    boost \
    openblas \
    protobuf-native \
    protobuf \
    glog \
    gflags \
    hdf5 \
    opencv \
    lmdb \
    snappy \
    leveldb \
    viennacl \
    ocl-icd \
    python3 \
    python3-native \
    python3-numpy-native \
"

inherit python3native

RDEPENDS_${PN} = "python3-numpy python3-imageio python3-six python3-protobuf"
RDEPENDS_${PN}-imagenet-model = "caffe-bvlc-reference"

LIC_FILES_CHKSUM = "file://LICENSE;md5=91d560803ea3d191c457b12834553991"

SRC_URI = " \
    git://github.com/BVLC/caffe.git;branch=opencl \
    file://0001-Allow-setting-numpy-include-dir-from-outside.patch \
    file://0002-cmake-do-not-use-SYSTEM-for-non-system-include-direc.patch \
    file://0003-cmake-fix-RPATHS.patch \
    file://0004-config-use-Python-3.patch \
    file://0005-io-change-to-imageio.patch \
    file://0006-classify-demo-added-a-demo-app-for-classifying-image.patch \
    file://0007-net_gen.py-fix-Python-3-compatibility.patch \
    file://0001-cmake-find-Yocto-boost-python-libs.patch \
"
SRCREV = "73221fd37a5499f809796fac2ea95daba1a8ce02"

SRC_URI[ilsvrc12.md5sum] = "f963098ea0e785a968ca1eb634003a90"
SRC_URI[ilsvrc12.sha256sum] = "e35c0c1994a21f7d8ed49d01881ce17ab766743d3b0372cdc0183ff4d0dfc491"

SRC_URI[caffenet.md5sum] = "af678f0bd3cdd2437e35679d88665170"
SRC_URI[caffenet.sha256sum] = "472d4a06035497b180636d8a82667129960371375bd10fcb6df5c6c7631f25e0"

S = "${WORKDIR}/git"

PACKAGES += "${PN}-imagenet-model"

RDEPENDS_${PN}-imagenet-model = "${PN}"

do_install_append() {
    # we need the BVLC ImageNet prototxt files for training & classification
    install -d ${D}${datadir}/Caffe/models/bvlc_reference_caffenet/
    install ${S}/models/bvlc_reference_caffenet/* ${D}${datadir}/Caffe/models/bvlc_reference_caffenet/
}

FILES_${PN}-imagenet-model += " \
    ${datadir}/Caffe/models/bvlc_reference_caffenet/* \
"
FILES_${PN} += " \
    ${prefix}/python/* \
"
FILES_${PN}-dev = " \
    ${includedir} \
    ${datadir}/Caffe/*cmake \
    ${libdir}/*.so \
"

inherit cmake python-dir

EXTRA_OECMAKE = " \
    -DBLAS=open \
    -DPYTHON_NUMPY_INCLUDE_DIR=${STAGING_DIR_TARGET}/usr/lib/python3.5/site-packages/numpy/core/include \
    -DPYTHON_EXECUTABLE=${STAGING_BINDIR_NATIVE}/python3-native/python3 \
    -DPYTHON_INCLUDE_DIRS=${STAGING_INCDIR_TARGET}/python3-native/python3.5m \
    -DPYTHON_LIBRARIES=${STAGING_LIBDIR_TARGET}/python3.5 \
"

